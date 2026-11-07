package com.baihonghong.tools;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于时间戳的36位ID生成器
 * 使用Snowflake算法变种，结合Base36编码
 *
 * @author baihonghong
 * @date 2026-11-07
 */
public class TimestampIdGenerator {

    /**
     * 开始时间戳 (2020-01-01 00:00:00)
     */
    private static final long START_TIMESTAMP = 1577808000000L;

    /**
     * 机器标识位数
     */
    private static final long MACHINE_ID_BITS = 10L;

    /**
     * 序列号位数
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 机器ID最大值
     */
    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_ID_BITS);

    /**
     * 序列号最大值
     */
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    /**
     * 机器ID左移位数
     */
    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 时间戳左移位数
     */
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;

    /**
     * Base36编码字符集
     */
    private static final char[] BASE36_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

    /**
     * 机器ID
     */
    private final long machineId;

    /**
     * 上次生成ID的时间戳
     */
    private long lastTimestamp = -1L;

    /**
     * 序列号
     */
    private long sequence = 0L;

    /**
     * 序列号计数器（用于生成36位）
     */
    private final AtomicLong counter = new AtomicLong(0);

    /**
     * 构造函数
     *
     * @param machineId 机器ID (0-1023)
     */
    public TimestampIdGenerator(long machineId) {
        if (machineId < 0 || machineId > MAX_MACHINE_ID) {
            throw new IllegalArgumentException(
                    String.format("机器ID必须在0-%d之间", MAX_MACHINE_ID));
        }
        this.machineId = machineId;
    }

    /**
     * 默认构造函数，使用机器ID为1
     */
    public TimestampIdGenerator() {
        this(1L);
    }

    /**
     * 生成36位ID
     *
     * @return 36位唯一ID字符串
     */
    public synchronized String generateId() {
        long currentTimestamp = System.currentTimeMillis();

        // 如果当前时间小于上次生成时间，说明时钟回拨
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("时钟回拨，拒绝生成ID。当前: %d, 上次: %d",
                            currentTimestamp, lastTimestamp));
        }

        // 如果是同一毫秒内
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 序列号溢出
            if (sequence == 0) {
                // 阻塞到下一毫秒
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            // 不同毫秒，序列号重置
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        // 生成原始ID
        long rawId = ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT)
                | (machineId << MACHINE_ID_SHIFT)
                | sequence;

        // 转换为36位字符串
        return encodeTo36(rawId);
    }

    /**
     * 将数字编码为36位字符串
     *
     * @param number 数字
     * @return 36位字符串
     */
    private String encodeTo36(long number) {
        StringBuilder sb = new StringBuilder();

        // Base36编码
        long temp = Math.abs(number);
        while (temp > 0) {
            sb.append(BASE36_CHARS[(int) (temp % 36)]);
            temp /= 36;
        }

        // 反转字符串
        String base36Str = sb.reverse().toString();

        // 如果长度不足36位，前面补随机字符
        if (base36Str.length() < 36) {
            int paddingLength = 36 - base36Str.length();
            StringBuilder padding = new StringBuilder();
            long counterValue = counter.getAndIncrement();

            for (int i = 0; i < paddingLength; i++) {
                int index = (int) ((counterValue + i) % 36);
                padding.append(BASE36_CHARS[index]);
            }
            return padding.toString() + base36Str;
        }

        // 如果长度超过36位，截取后36位
        if (base36Str.length() > 36) {
            return base36Str.substring(base36Str.length() - 36);
        }

        return base36Str;
    }

    /**
     * 阻塞到下一毫秒
     *
     * @param currentTimestamp 当前时间戳
     * @return 下一毫秒时间戳
     */
    private long waitNextMillis(long currentTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= currentTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        TimestampIdGenerator generator = new TimestampIdGenerator();

        System.out.println("生成10个36位ID:");
        for (int i = 0; i < 10; i++) {
            String id = generator.generateId();
            System.out.println(String.format("ID %d: %s (长度: %d)",
                    i + 1, id, id.length()));
        }

        // 性能测试
        System.out.println("\n性能测试：生成10000个ID");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            generator.generateId();
        }
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("耗时: %d 毫秒", endTime - startTime));
    }
}
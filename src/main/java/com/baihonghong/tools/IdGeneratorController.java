package com.baihonghong.tools;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * ID生成控制器
 *
 * @author baihonghong
 * @date 2026-11-07
 */
@RestController
public class IdGeneratorController {

    private final TimestampIdGenerator idGenerator = new TimestampIdGenerator();

    /**
     * 生成ID接口
     *
     * @return ID信息
     */
    @GetMapping("/generate")
    public Map<String, Object> generate() {
        String id = idGenerator.generateId();
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("length", id.length());
        result.put("timestamp", System.currentTimeMillis());
        result.put("success", true);
        return result;
    }

    /**
     * 批量生成ID接口
     *
     * @param count 生成数量
     * @return ID列表
     */
    @GetMapping("/generateBatch")
    public Map<String, Object> generateBatch(int count) {
        if (count <= 0 || count > 1000) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "数量必须在1-1000之间");
            return error;
        }

        String[] ids = new String[count];
        for (int i = 0; i < count; i++) {
            ids[i] = idGenerator.generateId();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("ids", ids);
        result.put("count", count);
        result.put("success", true);
        return result;
    }
}
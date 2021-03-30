package com.longge.controller;

import com.longge.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ContentController {
    @Autowired
    private ContentService contentService;

    /**
     * http://localhost:9090/parse/java
     * @param keyword
     * @return
     * @throws Exception
     */
    @GetMapping("/parse/{keyword}")
    public Boolean parse(@PathVariable("keyword")String keyword) throws Exception {
       return contentService.parseContent(keyword);
    }

    @GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
    public List<Map<String, Object>> parse(@PathVariable("keyword")String keyword,
                                           @PathVariable("pageNo")Integer pageNo,
                                           @PathVariable("pageSize")Integer pageSize) throws Exception {
        return contentService.searchPage(keyword, pageNo, pageSize);
    }
}

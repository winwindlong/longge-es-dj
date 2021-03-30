package com.longge.controller;

import com.longge.pojo.Goods;
import com.longge.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/goods/createIndex")
    public Boolean createIndex() throws Exception {
        return goodsService.createIndexRest();
    }

    @GetMapping("/goods/find/{id}")
    public Goods findGoods(@PathVariable("id")long id) throws Exception {
        return goodsService.findById(id);
    }
    @GetMapping("/goods/exists/{id}")
    public Boolean existsGoods(@PathVariable("id")long id) throws Exception {
        return goodsService.existsById(id);
    }

    @GetMapping("/goods/page/{keyword}/{pageNo}/{pageSize}")
    public Page<Goods> page(@PathVariable("keyword")String keyword,
                            @PathVariable("pageNo")int pageNo,
                            @PathVariable("pageSize")int pageSize) throws Exception {
        return goodsService.search(keyword, pageNo, pageSize);
    }
}

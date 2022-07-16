package com.metabubble.BWC.controller;

import com.metabubble.BWC.common.R;
import com.metabubble.BWC.entity.Merchant;
import com.metabubble.BWC.mapper.MerchantMapper;
import com.metabubble.BWC.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/merchant")
@Slf4j
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

    @PostMapping
    public R<String> save(@RequestBody Merchant merchant){
        merchantService.save(merchant);
        return R.success("添加成功");
    }
}

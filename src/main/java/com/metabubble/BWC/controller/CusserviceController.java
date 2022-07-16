package com.metabubble.BWC.controller;

import com.metabubble.BWC.common.R;
import com.metabubble.BWC.entity.Cusservice;
import com.metabubble.BWC.entity.User;
import com.metabubble.BWC.service.ConfigService;
import com.metabubble.BWC.service.CusserviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cusservice")
@Slf4j
public class CusserviceController {

    @Autowired
    private CusserviceService cusserviceService;



}

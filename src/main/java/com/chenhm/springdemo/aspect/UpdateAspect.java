package com.chenhm.springdemo.aspect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.diff.JsonDiff;
import com.google.common.collect.Lists;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Frank on 8/15/2016.
 */
@Aspect
@Component
public class UpdateAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SimpMessagingTemplate messaging;

    @Around("(execution(* save(..)) || execution(* delete(..))) && target(repository)")
    public Object publishChange(ProceedingJoinPoint jp, CrudRepository repository) throws Throwable {
        logger.info("publishChange " + jp);
        List original = Lists.newArrayList(repository.findAll());
        Object ret = jp.proceed();
        List updated = Lists.newArrayList(repository.findAll());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode patchNode = JsonDiff.asJson(mapper.valueToTree(original), mapper.valueToTree(updated));

        messaging.convertAndSend("/topic/todoes", patchNode);
        return ret;
    }
}

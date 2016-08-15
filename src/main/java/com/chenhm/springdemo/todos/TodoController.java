package com.chenhm.springdemo.todos;

import com.chenhm.springdemo.aspect.AroundLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Frank on 8/13/2016.
 */
@AroundLog(level = "debug")
@RestController
@RequestMapping("/rest/")
public class TodoController {

    @Autowired
    TodoRepository todoRepository;

    @RequestMapping(value = "todoes", produces = MediaType.APPLICATION_JSON_VALUE )
    @SubscribeMapping("/todoes")
    public Iterable<Todo> findAll(){
        return todoRepository.findAll();
    }

    @AroundLog
    @RequestMapping(value = "todoes/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public Todo findOne(@PathVariable Long id){
        return todoRepository.findOne(id);
    }

    /**
     * a example using {@link ModelAndView }
     * @return
     */
    @RequestMapping(value = "todo.html", produces = MediaType.TEXT_HTML_VALUE )
    public ModelAndView todo_html(){
        return new ModelAndView("todo").addObject("todoList", todoRepository.findAll());
    }
}

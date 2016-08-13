package com.chenhm.springdemo.todos;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Frank on 8/13/2016.
 */
public interface TodoRepository extends CrudRepository<Todo, Long> {}

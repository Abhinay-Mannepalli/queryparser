package com.queryparser.controllar;

import com.queryparser.Model.QueryRequest;
import com.queryparser.Model.Response;
import com.queryparser.service.QueryParserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = {"/api"})
public class QueryParserController {

    @Autowired
    private QueryParserService service;

    @PostMapping(value="/query", produces="application/json", consumes="application/json")
    public ResponseEntity<String> executeQuery(@RequestBody QueryRequest request) {
        System.out.println(request.getQuery());
        try{
            String[] list = request.getQuery().split(" ");
            if(list[0].equals(QueryType.SELECT.getKey()) || list[0].equals(QueryType.SELECT.getValue())
                || list[0].equals(QueryType.DESC.getKey()) || list[0].equals(QueryType.DESC.getValue())) {
                System.out.println("In first if");
                Response result = service.executeSelectQuery(request.getQuery());
                return new ResponseEntity<>(result.getResponse(),result.getStatus());
            } else if(list[0].equals(QueryType.UPDATE.getKey()) || list[0].equals(QueryType.UPDATE.getValue())
                        || list[0].equals(QueryType.DELETE.getKey()) || list[0].equals(QueryType.DELETE.getValue())
                        || list[0].equals(QueryType.INSERT.getKey()) || list[0].equals(QueryType.INSERT.getValue())){
                System.out.println("In second if");
                Response result = service.executeDataManipulationQuery(request.getQuery());
                return new ResponseEntity<>(result.getResponse(),result.getStatus());
            } else  if(list[0].equals(QueryType.DROP.getKey()) || list[0].equals(QueryType.DROP.getValue())
                    || list[0].equals(QueryType.CREATE.getKey()) || list[0].equals(QueryType.CREATE.getValue())
                    || list[0].equals(QueryType.ALTER.getKey()) || list[0].equals(QueryType.ALTER.getValue())){
                System.out.println("In third if");
                Response result = service.executeDataDefinitionQuery(request.getQuery());
                return new ResponseEntity<>(result.getResponse(),result.getStatus());
            } else  {
                System.out.println("In else");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
}
}

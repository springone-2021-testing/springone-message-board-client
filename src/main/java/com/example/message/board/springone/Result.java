package com.example.message.board.springone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private  String status;
    private  String operation;
    private  String parameter;

}

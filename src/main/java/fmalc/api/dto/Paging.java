package fmalc.api.dto;

import lombok.Data;

import java.util.*;

@Data
public class Paging {
    private List list;
    private int totalPage;
    private int pageCurrent;
    private  int numberElements=10;

}

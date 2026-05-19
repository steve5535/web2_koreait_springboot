package com.study.koreait.diAndIoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IocService {
    // @Autowired - 호출될때 의존성주입
    private IocRepository repository;

    @Autowired // Bean 생성시 의존성주입
    public IocService(IocRepository repository) {
        this.repository = repository;
    }
    /*
        A를 B를 필드로 가짐
        B도 A를 필드로 가짐
        A를 만드려고하니 B가 필요 -> B생성
        B를 만드려고하니 A가 필요 -> A생성 ... 반복
     */

    public int getTotal() {
        int total = 0;
        for (Integer score : repository.getScores()) {
            total += score;
        }
        return total;
    }

    public double getAvg() {
        return (double) getTotal() / repository.getScores().size();
    }

}

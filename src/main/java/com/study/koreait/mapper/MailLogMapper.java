package com.study.koreait.mapper;

import com.study.koreait.entity.MailAuditLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MailLogMapper {
    int addLog(MailAuditLog log);
}

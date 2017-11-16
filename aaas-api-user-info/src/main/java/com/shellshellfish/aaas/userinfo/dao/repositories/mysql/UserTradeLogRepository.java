package com.shellshellfish.aaas.userinfo.dao.repositories.mysql;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiTrdLog;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserTradeLogRepository extends PagingAndSortingRepository<UiTrdLog, Long> {

  Page<UiTrdLog> findAllByUserId(Long userId, Pageable pageRequest);
}

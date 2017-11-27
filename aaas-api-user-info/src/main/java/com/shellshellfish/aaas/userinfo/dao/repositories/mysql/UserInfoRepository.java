package com.shellshellfish.aaas.userinfo.dao.repositories.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shellshellfish.aaas.userinfo.model.dao.UiUser;

@Repository
public interface UserInfoRepository extends PagingAndSortingRepository<UiUser, Long> {

  UiUser findById(long id);

  @Query(value ="SELECT id FROM ui_user WHERE uuid =:uuid", nativeQuery = true)
  public Long findUserIdByUuid(String uuid);

  public UiUser findUiUserByUuid(String uuid);
}

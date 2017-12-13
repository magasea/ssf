package com.shellshellfish.aaas.account.repositories.mysql2;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.shellshellfish.aaas.account.model.dao2.UiAsset;

public interface AssetRepository extends JpaRepository<UiAsset, Long> {
	List<UiAsset> findById(String id);
}

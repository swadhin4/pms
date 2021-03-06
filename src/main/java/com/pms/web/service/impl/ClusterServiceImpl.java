package com.pms.web.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.jpa.entities.Cluster;
import com.pms.jpa.repositories.ClusterRepo;
import com.pms.web.service.ClusterService;



@Service("clusterService")
public class ClusterServiceImpl implements ClusterService{


	@Autowired
	private ClusterRepo clusterRepo;

	@Override
	public List<Cluster> getAllClusters() {
		return clusterRepo.findAll();
	}

	@Override
	public List<Cluster> getAllClustersBy(Long districtId, Long areaId)	throws Exception {
		List<Cluster> clusterList = clusterRepo.findByDistrictIdAndArea(districtId, areaId);
		return clusterList == null?Collections.EMPTY_LIST:clusterList;
	}



}

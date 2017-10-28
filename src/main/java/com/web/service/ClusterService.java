package com.web.service;

import java.util.List;

import com.jpa.entities.Cluster;


public interface ClusterService {


	public List<Cluster> getAllClusters();

	public List<Cluster> getAllClustersBy(Long districtId, Long areaId) throws Exception;

}

package com.pms.web.service;

import java.util.List;

import com.pms.jpa.entities.SlaApproachingView;

public interface SlaApproachingViewService {

	public List<SlaApproachingView> findTicketsApproachingSLA(int noOfDays);


	public List<SlaApproachingView> findTicketsApproachingSLA();

}

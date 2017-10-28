package com.web.service;

import java.util.List;

import com.jpa.entities.SlaApproachingView;

public interface SlaApproachingViewService {

	public List<SlaApproachingView> findTicketsApproachingSLA(int noOfDays);


	public List<SlaApproachingView> findTicketsApproachingSLA();

}

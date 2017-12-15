package com.pms.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pms.jpa.entities.TicketComment;

public interface TicketCommentRepo extends JpaRepository<TicketComment, Long> {

	public List<TicketComment> findByTicketId(Long ticketId);

}

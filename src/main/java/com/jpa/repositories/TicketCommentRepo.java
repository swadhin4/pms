package com.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jpa.entities.TicketComment;

public interface TicketCommentRepo extends JpaRepository<TicketComment, Long> {

	public List<TicketComment> findByTicketId(Long ticketId);

}

package co.simplon.springticketapi.controller;

import co.simplon.springticketapi.dao.TicketDao;
import co.simplon.springticketapi.model.Ticket;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/tickets")
@RestController

public class TicketController {

    private final TicketDao ticketDao;

    public TicketController(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    @GetMapping("/byclassroom/{id}")
    public List<Ticket> getAllTickets(@PathVariable Long id) {
        return ticketDao.getAllByClassroom(id);
    }

    @GetMapping("/{id}")
    public Ticket getTicket(@PathVariable Long id) {
        return ticketDao.get(id);
    }

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket) {
        return ticketDao.save(ticket);
    }

    @PutMapping("/{id}")
    public Ticket updateTicket(@PathVariable Long id, @RequestBody Ticket ticket) {
        if (ticket.getId().equals(id))
            return ticketDao.updateTicketStatus(ticket, id);
        else
            return null;
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        ticketDao.delete(id);
    }
}

package co.simplon.springticketapi.dao;

import co.simplon.springticketapi.model.Learner;
import co.simplon.springticketapi.model.Ticket;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
public class LearnerDao implements Dao<Learner> {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Learner> learnerRowMapper;

    public LearnerDao(JdbcTemplate jdbcTemplate, RowMapper<Learner> learnerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.learnerRowMapper = learnerRowMapper;
    }

    @Override
    public Learner get(Long id) {
        return jdbcTemplate.queryForObject("select * from learner where id = ?",
                learnerRowMapper, id);
    }

    @Override
    public List<Learner> getAll() {
        return jdbcTemplate.query("select * from learner", learnerRowMapper);
    }

    public List<Learner> getAllByClassroom(int classroomId) {
        return jdbcTemplate.query("select * from learner where classroom_idx = ?", learnerRowMapper, classroomId);
    }

    @Override
    public Learner save(Learner learner) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("insert into learner (first_name, last_name, classroom_idx) values (?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, learner.getFirstName());
            ps.setString(2, learner.getLastName());
            ps.setLong(3, learner.getClassroomIdx());
            return ps;
        }, keyHolder);

        Long id = ((Integer) keyHolder.getKeys().get("id")).longValue();
        learner.setId(id);
        return learner;
    }

    @Override
    public void delete(Long id) {
        String deleteStatement = "DELETE FROM learner WHERE id = ?";
        jdbcTemplate.update(deleteStatement, id);
    }

    public Learner getMostHelped() {
        String sqlQuery = "select count(*) as count, l.id as id, l.first_name as first_name, l.last_name as last_name, l.classroom_idx as classroom_idx\n" +
                "from ticket\n" +
                "join learner l on ticket.learner_idx = l.id\n" +
                "group by learner_idx, l.first_name, l.id, l.last_name, l.classroom_idx\n" +
                "order by count desc limit 1;";

        return jdbcTemplate.queryForObject(sqlQuery, learnerRowMapper);
    }
}

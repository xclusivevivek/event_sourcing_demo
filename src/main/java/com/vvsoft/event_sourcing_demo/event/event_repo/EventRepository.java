package com.vvsoft.event_sourcing_demo.event.event_repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vvsoft.event_sourcing_demo.event.Event;
import com.vvsoft.event_sourcing_demo.event.EventWrapper;
import org.hibernate.type.SqlTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class EventRepository<T extends Event> {

    Logger logger = LoggerFactory.getLogger(EventRepository.class);

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper;

    @Autowired
    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
    }

    public void save(T event, String streamId, String eventName, int version) {
        EventWrapper<T> wrappedEvent = wrapEvent(event,streamId,version);
        String insertEventQuery = "insert into events(\"id\",\"type\",stream_id,\"version\",event_received,event_occurred,\"data\") values (?,?,?,?,?,?,?)";
        try {
            String serializedEvent = objectMapper.writeValueAsString(event);
            logger.info("Writing Event : {}", serializedEvent);
            Object[] params = List.of(wrappedEvent.getId(), eventName, streamId, wrappedEvent.getVersion(), wrappedEvent.getCreatedAt(), wrappedEvent.getCreatedAt(), serializedEvent).toArray();
            int[] types = new int[]{SqlTypes.VARCHAR, SqlTypes.VARCHAR, SqlTypes.VARCHAR, SqlTypes.INTEGER, SqlTypes.TIMESTAMP, SqlTypes.TIMESTAMP, SqlTypes.VARCHAR};
            jdbcTemplate.update(insertEventQuery,params,types);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public List<EventWrapper<T>> read(String streamId) {
        String readQuery = "select \"id\",\"type\",stream_id,\"version\",event_received,event_occurred,\"data\" from events.events where stream_id='" + streamId + "' order by version";
        return jdbcTemplate.query(readQuery, new RowMapper<EventWrapper<T>>() {
            @Override
            public EventWrapper<T> mapRow(ResultSet rs, int rowNum) throws SQLException {
                EventWrapper<T> wrapper = new EventWrapper<>();
                try {
                    wrapper.setEvent(objectMapper.readValue(rs.getString("data"), new TypeReference<T>() {}));
                    wrapper.setId(rs.getString("id"));
                    wrapper.setVersion(rs.getInt("version"));
                    wrapper.setStreamId(rs.getString("stream_id"));
                    wrapper.setCreatedAt(rs.getTimestamp("event_received").toLocalDateTime());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                return wrapper;
            }
        });

    }

    private EventWrapper<T> wrapEvent(T event,String streamId,int version) {
        EventWrapper<T> wrappedEvent = new EventWrapper<>();
        wrappedEvent.setId(UUID.randomUUID().toString());
        wrappedEvent.setVersion(version);
        wrappedEvent.setStreamId(streamId);
        wrappedEvent.setCreatedAt(LocalDateTime.now());
        return wrappedEvent;
    }
}

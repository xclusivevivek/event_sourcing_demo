CREATE TABLE events.events (
    id  varchar(50) PRIMARY KEY,
    type varchar(50),
    stream_id varchar(50),
    version int,
    event_received  timestamp,
    event_occurred  timestamp,
    data varchar(500),
    UNIQUE (stream_id,version)
);


Build the state of an entity from existing en


package service.fs;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;

import java.time.Instant;

public class FsItemEx {

    public String name;
    public String mime;
    @JsonSerialize(using = InstantSerializer.class)
    public Instant ts;
    public int read;
    public int write;
    public int locked;
    public Long size;
    public String hash;
    public String phash;
    public String volumeid;
    public Integer dirs;
    public String url;
}

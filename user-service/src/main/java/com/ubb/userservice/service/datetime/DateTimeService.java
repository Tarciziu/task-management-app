package com.ubb.userservice.service.datetime;

import java.time.Instant;
import java.time.LocalDateTime;

public interface DateTimeService {
    default LocalDateTime now() {
        return LocalDateTime.now();
    }

    default Instant currentTime() {
        return Instant.now();
    }
}

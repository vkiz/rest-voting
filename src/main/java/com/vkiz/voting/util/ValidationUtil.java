package com.vkiz.voting.util;

import com.vkiz.voting.HasDate;
import com.vkiz.voting.HasId;
import com.vkiz.voting.util.exception.IllegalRequestDataException;
import com.vkiz.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(HasId entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.getId() != id) {
            throw new IllegalRequestDataException(entity + " must be with id=" + id);
        }
    }

    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }

    public static <T extends HasDate> T checkDate(T t) {
        final LocalDate date = t.getDate();
        if (date == null) {
            t.setDate(LocalDate.now());
        } else if (!date.isEqual(LocalDate.now())) {
            throw new IllegalRequestDataException(t + " date=" + date + " must be today: " + LocalDate.now());
        }
        return t;
    }

    public static <T extends HasDate> T checkDateAndIsVoteChangeTimeExpired(T t, boolean checkExpiredTime) {
        checkDate(t);
        if (checkExpiredTime && isVoteChangeTimeExpired()) {
            throw new IllegalRequestDataException("Vote change time expired at 11:00 AM");
        }
        return t;
    }

    public static boolean isVoteChangeTimeExpired() {
        return LocalTime.now().isAfter(LocalTime.of(11, 0));
    }
}

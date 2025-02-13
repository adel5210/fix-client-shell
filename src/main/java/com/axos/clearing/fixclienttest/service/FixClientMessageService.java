package com.axos.clearing.fixclienttest.service;

import com.axos.clearing.fixclienttest.utils.FixUtilz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import quickfix.BooleanField;
import quickfix.DoubleField;
import quickfix.IntField;
import quickfix.StringField;
import quickfix.field.CheckSum;
import quickfix.field.MsgSeqNum;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@ShellComponent
@RequiredArgsConstructor
@Slf4j
public class FixClientMessageService {

    private final FixClientConnectivityService fixClientConnectivityService;

    private final AtomicReference<String> senderCompID = new AtomicReference<>();
    private final AtomicReference<String> targetCompID = new AtomicReference<>();
    private final AtomicInteger msgSeqNum = new AtomicInteger(0);
    private final AtomicReference<quickfix.fix42.Message> message42 = new AtomicReference<>(new quickfix.fix42.Message());
    private final AtomicReference<quickfix.fix44.Message> message44 = new AtomicReference<>(new quickfix.fix44.Message());
    private final AtomicInteger fixType = new AtomicInteger();

    @ShellMethod(key = "sender-comp-id", value = "Set the Sender CompID.")
    public void setSenderCompID(String senderCompID) {
        this.senderCompID.set(senderCompID);
        log.info("Sender CompID set to: {}", senderCompID);
    }

    @ShellMethod(key = "target-comp-id", value = "Set the Target CompID.")
    public void setTargetCompID(String targetCompID) {
        this.targetCompID.set(targetCompID);
        log.info("Target CompID set to: {}", targetCompID);
    }

    @ShellMethod(key = "msg-seq-num", value = "Set the Message Sequence Number.")
    public void setMsgSeqNum(int msgSeqNum) {
        this.msgSeqNum.set(msgSeqNum);
        log.info("Message Sequence Number set to: {}", msgSeqNum);
    }

    @ShellMethod(key = "fix-type", value = "Set the FIX protocol version (42 or 44).")
    public void setFixType(final int fixType) {
        this.fixType.set(fixType);
        log.info("Fix Type set to: {}", fixType);
    }

    @ShellMethod(key = "send-raw", value = "Send a raw FIX message.")
    public void sendRawMessage(String message) {
        log.info("Processing raw fix message: {}", message);
        fixClientConnectivityService.send(message);
    }

    @ShellMethod(key = "reset-message", value = "Reset the current FIX message.")
    public void resetFixMessage() {
        message42.set(new quickfix.fix42.Message());
        message44.set(new quickfix.fix44.Message());
        log.info("Resetting Fix Message");
    }

    @ShellMethod(key = "add-int-tag", value = "Add an integer tag to the FIX message.")
    public void addIntTag(final int tagNo, final int tagVal, final String type) {
        if (type.equalsIgnoreCase("header")) {
            if (fixType.get() == 42) {
                message42.get().getHeader().setField((new IntField(tagNo, tagVal)));
            } else if (fixType.get() == 44) {
                message44.get().getHeader().setField((new IntField(tagNo, tagVal)));
            }
        } else if (type.equalsIgnoreCase("trailer")) {
            if (fixType.get() == 42) {
                message42.get().getTrailer().setField((new IntField(tagNo, tagVal)));
            } else if (fixType.get() == 44) {
                message44.get().getTrailer().setField((new IntField(tagNo, tagVal)));
            }
        } else {
            if (fixType.get() == 42) {
                message42.get().setInt(tagNo, tagVal);
            } else if (fixType.get() == 44) {
                message44.get().setInt(tagNo, tagVal);
            }
        }
    }

    @ShellMethod(key = "add-str-tag", value = "Add a string tag to the FIX message.")
    public void addStringTag(final int tagNo, final String tagVal, final String type) {
        if (type.equalsIgnoreCase("header")) {
            if (fixType.get() == 42) {
                message42.get().getHeader().setField((new StringField(tagNo, tagVal)));
            } else if (fixType.get() == 44) {
                message44.get().getHeader().setField((new StringField(tagNo, tagVal)));
            }
        } else if (type.equalsIgnoreCase("trailer")) {
            if (fixType.get() == 42) {
                message42.get().getTrailer().setField((new StringField(tagNo, tagVal)));
            } else if (fixType.get() == 44) {
                message44.get().getTrailer().setField((new StringField(tagNo, tagVal)));
            }
        } else {
            if (fixType.get() == 42) {
                message42.get().setString(tagNo, tagVal);
            } else if (fixType.get() == 44) {
                message44.get().setString(tagNo, tagVal);
            }
        }
    }

    @ShellMethod(key = "add-double-tag", value = "Add a double tag to the FIX message.")
    public void addDoubleTag(final int tagNo, final Double tagVal, final String type) {
        if (type.equalsIgnoreCase("header")) {
            if (fixType.get() == 42) {
                message42.get().getHeader().setField((new DoubleField(tagNo, tagVal)));
            } else if (fixType.get() == 44) {
                message44.get().getHeader().setField((new DoubleField(tagNo, tagVal)));
            }
        } else if (type.equalsIgnoreCase("trailer")) {
            if (fixType.get() == 42) {
                message42.get().getTrailer().setField((new DoubleField(tagNo, tagVal)));
            } else if (fixType.get() == 44) {
                message44.get().getTrailer().setField((new DoubleField(tagNo, tagVal)));
            }
        } else {
            if (fixType.get() == 42) {
                message42.get().setDouble(tagNo, tagVal);
            } else if (fixType.get() == 44) {
                message44.get().setDouble(tagNo, tagVal);
            }
        }
    }

    @ShellMethod(key = "add-bool-tag", value = "Add a boolean tag to the FIX message.")
    public void addBoolTag(final int tagNo, final Boolean tagVal, final String type) {
        if (type.equalsIgnoreCase("header")) {
            if (fixType.get() == 42) {
                message42.get().getHeader().setField((new BooleanField(tagNo, tagVal)));
            } else if (fixType.get() == 44) {
                message44.get().getHeader().setField((new BooleanField(tagNo, tagVal)));
            }
        } else if (type.equalsIgnoreCase("trailer")) {
            if (fixType.get() == 42) {
                message42.get().getTrailer().setField((new BooleanField(tagNo, tagVal)));
            } else if (fixType.get() == 44) {
                message44.get().getTrailer().setField((new BooleanField(tagNo, tagVal)));
            }
        } else {
            if (fixType.get() == 42) {
                message42.get().setBoolean(tagNo, tagVal);
            } else if (fixType.get() == 44) {
                message44.get().setBoolean(tagNo, tagVal);
            }
        }
    }

    @ShellMethod(key = "send-fix", value = "Send the constructed FIX message.")
    public void sendFixMessage() {
        if (fixType.get() == 42) {
            message42.get().getHeader().setField(new MsgSeqNum(msgSeqNum.getAndIncrement()));
            message42.get().getTrailer().setField(new CheckSum(String.valueOf(FixUtilz.checksum42(message42.get()))));
            log.info("Sending message42: {}", message42.get().toString());
            fixClientConnectivityService.send(message42.get().toString());
        } else if (fixType.get() == 44) {
            message44.get().getHeader().setField(new MsgSeqNum(msgSeqNum.getAndIncrement()));
            message44.get().getTrailer().setField(new CheckSum(String.valueOf(FixUtilz.checksum44(message44.get()))));
            log.info("Sending message44: {}", message44.get().toString());
            fixClientConnectivityService.send(message44.get().toString());
        }
    }
}
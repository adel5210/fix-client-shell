package com.axos.clearing.fixclienttest.service;

import com.axos.clearing.fixclienttest.FixClientTestApplication;
import com.axos.clearing.fixclienttest.utils.FixUtilz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import quickfix.BooleanField;
import quickfix.DoubleField;
import quickfix.IntField;
import quickfix.StringField;
import quickfix.field.*;

import java.time.LocalDateTime;

@ShellComponent
@RequiredArgsConstructor
@Slf4j
public class FixClientMessageService {

    private final FixClientTestApplication fixClientTestApplication;
    private final FixClientConnectivityService fixClientConnectivityService;


    @ShellMethod(key = "sender-comp-id", value = "Set the Sender CompID.")
    public void setSenderCompID(String senderCompID) {
        fixClientTestApplication.senderCompID.set(senderCompID);
        log.info("Sender CompID set to: {}", senderCompID);
    }

    @ShellMethod(key = "target-comp-id", value = "Set the Target CompID.")
    public void setTargetCompID(String targetCompID) {
        fixClientTestApplication.targetCompID.set(targetCompID);
        log.info("Target CompID set to: {}", targetCompID);
    }

    @ShellMethod(key = "msg-seq-num", value = "Set the Message Sequence Number.")
    public void setMsgSeqNum(int msgSeqNum) {
        fixClientTestApplication.msgSeqNum.set(msgSeqNum);
        log.info("Message Sequence Number set to: {}", msgSeqNum);
    }

    @ShellMethod(key = "fix-type", value = "Set the FIX protocol version (42 or 44).")
    public void setFixType(final int fixType) {
        fixClientTestApplication.fixType.set(fixType);
        log.info("Fix Type set to: {}", fixType);
    }

    @ShellMethod(key = "send-raw", value = "Send a raw FIX message.")
    public void sendRawMessage(String message) {
        log.info("Processing raw fix message: {}", message);
        fixClientConnectivityService.send(message);
    }

    @ShellMethod(key = "reset-message", value = "Reset the current FIX message.")
    public void resetFixMessage() {
        fixClientTestApplication.message42.set(new quickfix.fix42.Message());
        fixClientTestApplication.message44.set(new quickfix.fix44.Message());
        log.info("Resetting Fix Message");
    }

    @ShellMethod(key = "add-int-tag", value = "Add an integer tag to the FIX message.")
    public void addIntTag(final int tagNo, final int tagVal, final String type) {
        if (type.equalsIgnoreCase("header")) {
            if (fixClientTestApplication.fixType.get() == 42) {
                fixClientTestApplication.message42.get().getHeader().setField((new IntField(tagNo, tagVal)));
            } else if (fixClientTestApplication.fixType.get() == 44) {
                fixClientTestApplication.message44.get().getHeader().setField((new IntField(tagNo, tagVal)));
            }
        } else if (type.equalsIgnoreCase("trailer")) {
            if (fixClientTestApplication.fixType.get() == 42) {
                fixClientTestApplication.message42.get().getTrailer().setField((new IntField(tagNo, tagVal)));
            } else if (fixClientTestApplication.fixType.get() == 44) {
                fixClientTestApplication.message44.get().getTrailer().setField((new IntField(tagNo, tagVal)));
            }
        } else {
            if (fixClientTestApplication.fixType.get() == 42) {
                fixClientTestApplication.message42.get().setInt(tagNo, tagVal);
            } else if (fixClientTestApplication.fixType.get() == 44) {
                fixClientTestApplication.message44.get().setInt(tagNo, tagVal);
            }
        }
    }

    @ShellMethod(key = "add-str-tag", value = "Add a string tag to the FIX message.")
    public void addStringTag(final int tagNo, final String tagVal, final String type) {
        if (type.equalsIgnoreCase("header")) {
            if (fixClientTestApplication.fixType.get() == 42) {
                fixClientTestApplication.message42.get().getHeader().setField((new StringField(tagNo, tagVal)));
            } else if (fixClientTestApplication.fixType.get() == 44) {
                fixClientTestApplication.message44.get().getHeader().setField((new StringField(tagNo, tagVal)));
            }
        } else if (type.equalsIgnoreCase("trailer")) {
            if (fixClientTestApplication.fixType.get() == 42) {
                fixClientTestApplication.message42.get().getTrailer().setField((new StringField(tagNo, tagVal)));
            } else if (fixClientTestApplication.fixType.get() == 44) {
                fixClientTestApplication.message44.get().getTrailer().setField((new StringField(tagNo, tagVal)));
            }
        } else {
            if (fixClientTestApplication.fixType.get() == 42) {
                fixClientTestApplication.message42.get().setString(tagNo, tagVal);
            } else if (fixClientTestApplication.fixType.get() == 44) {
                fixClientTestApplication.message44.get().setString(tagNo, tagVal);
            }
        }
    }

    @ShellMethod(key = "add-double-tag", value = "Add a double tag to the FIX message.")
    public void addDoubleTag(final int tagNo, final Double tagVal, final String type) {
        if (type.equalsIgnoreCase("header")) {
            if (fixClientTestApplication.fixType.get() == 42) {
                fixClientTestApplication.message42.get().getHeader().setField((new DoubleField(tagNo, tagVal)));
            } else if (fixClientTestApplication.fixType.get() == 44) {
                fixClientTestApplication.message44.get().getHeader().setField((new DoubleField(tagNo, tagVal)));
            }
        } else if (type.equalsIgnoreCase("trailer")) {
            if (fixClientTestApplication.fixType.get() == 42) {
                fixClientTestApplication.message42.get().getTrailer().setField((new DoubleField(tagNo, tagVal)));
            } else if (fixClientTestApplication.fixType.get() == 44) {
                fixClientTestApplication.message44.get().getTrailer().setField((new DoubleField(tagNo, tagVal)));
            }
        } else {
            if (fixClientTestApplication.fixType.get() == 42) {
                fixClientTestApplication.message42.get().setDouble(tagNo, tagVal);
            } else if (fixClientTestApplication.fixType.get() == 44) {
                fixClientTestApplication.message44.get().setDouble(tagNo, tagVal);
            }
        }
    }

    @ShellMethod(key = "add-bool-tag", value = "Add a boolean tag to the FIX message.")
    public void addBoolTag(final int tagNo, final Boolean tagVal, final String type) {
        if (type.equalsIgnoreCase("header")) {
            if (fixClientTestApplication.fixType.get() == 42) {
                fixClientTestApplication.message42.get().getHeader().setField((new BooleanField(tagNo, tagVal)));
            } else if (fixClientTestApplication.fixType.get() == 44) {
                fixClientTestApplication.message44.get().getHeader().setField((new BooleanField(tagNo, tagVal)));
            }
        } else if (type.equalsIgnoreCase("trailer")) {
            if (fixClientTestApplication.fixType.get() == 42) {
                fixClientTestApplication.message42.get().getTrailer().setField((new BooleanField(tagNo, tagVal)));
            } else if (fixClientTestApplication.fixType.get() == 44) {
                fixClientTestApplication.message44.get().getTrailer().setField((new BooleanField(tagNo, tagVal)));
            }
        } else {
            if (fixClientTestApplication.fixType.get() == 42) {
                fixClientTestApplication.message42.get().setBoolean(tagNo, tagVal);
            } else if (fixClientTestApplication.fixType.get() == 44) {
                fixClientTestApplication.message44.get().setBoolean(tagNo, tagVal);
            }
        }
    }

    @ShellMethod(key = "send-fix", value = "Send the constructed FIX message.")
    public void sendFixMessage() {
        if (fixClientTestApplication.fixType.get() == 42) {
            fixClientTestApplication.message42.get().getHeader().setField(new BeginString("FIX.4.2"));
            fixClientTestApplication.message42.get().getHeader().setField(new MsgSeqNum(fixClientTestApplication.msgSeqNum.getAndIncrement()));
            fixClientTestApplication.message42.get().getHeader().setField(new SenderCompID(fixClientTestApplication.senderCompID.get()));
            fixClientTestApplication.message42.get().getHeader().setField(new TargetCompID(fixClientTestApplication.targetCompID.get()));
            fixClientTestApplication.message42.get().getHeader().setField(new SendingTime(LocalDateTime.now()));
            fixClientTestApplication.message42.get().getHeader().setField(new BodyLength());
            fixClientTestApplication.message42.get().getTrailer().setField(new CheckSum(String.valueOf(FixUtilz.checksum42(fixClientTestApplication.message42.get()))));
            log.info("Sending message42: {}", fixClientTestApplication.message42.get().toString());
            fixClientConnectivityService.send(fixClientTestApplication.message42.get().toString());
        } else if (fixClientTestApplication.fixType.get() == 44) {
            fixClientTestApplication.message42.get().getHeader().setField(new BeginString("FIX.4.4"));
            fixClientTestApplication.message44.get().getHeader().setField(new MsgSeqNum(fixClientTestApplication.msgSeqNum.getAndIncrement()));
            fixClientTestApplication.message44.get().getHeader().setField(new SenderCompID(fixClientTestApplication.senderCompID.get()));
            fixClientTestApplication.message44.get().getHeader().setField(new TargetCompID(fixClientTestApplication.targetCompID.get()));
            fixClientTestApplication.message44.get().getHeader().setField(new SendingTime(LocalDateTime.now()));
            fixClientTestApplication.message44.get().getHeader().setField(new BodyLength());
            fixClientTestApplication.message44.get().getTrailer().setField(new CheckSum(String.valueOf(FixUtilz.checksum44(fixClientTestApplication.message44.get()))));
            log.info("Sending message44: {}", fixClientTestApplication.message44.get().toString());
            fixClientConnectivityService.send(fixClientTestApplication.message44.get().toString());
        }
    }
}
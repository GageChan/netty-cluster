package com.gagechan.socket.protocol;

public enum Command {
    UN_SUPPORT_COMMAND(0),HEARTBEAT(1), HEALTHY(2);

    private Integer code;

    Command(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static Command from(int i) {
        Command[] commands = Command.values();
        for (Command command : commands) {
            if (command.getCode() == i) {
                return command;
            }
        }
        return Command.UN_SUPPORT_COMMAND;
    }

}

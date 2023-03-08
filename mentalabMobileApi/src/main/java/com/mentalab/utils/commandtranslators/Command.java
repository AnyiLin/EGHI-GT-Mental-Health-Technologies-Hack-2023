package com.mentalab.utils.commandtranslators;

public enum Command {
  CMD_SAMPLING_RATE_SET(0xA1) {
    @Override
    public CommandTranslator createCommandTranslator() {
      return new TwoByteCommandTranslator(this.getOperationCode(), this.getArg());
    }
  },
  CMD_CHANNEL_SET(0xA2) {
    @Override
    public CommandTranslator createCommandTranslator() {
      return new TwoByteCommandTranslator(this.getOperationCode(), this.getArg());
    }
  },
  CMD_MEMORY_FORMAT(0xA3) {
    @Override
    public CommandTranslator createCommandTranslator() {
      return new TwoByteCommandTranslator(this.getOperationCode(), this.getArg());
    }
  },
  CMD_REC_TIME_SET(0xB1) {
    @Override
    public CommandTranslator createCommandTranslator() {
      return null;
    }
  },
  CMD_MODULE_DISABLE(0xA4) {
    @Override
    public CommandTranslator createCommandTranslator() {
      return new TwoByteCommandTranslator(this.getOperationCode(), this.getArg());
    }
  },
  CMD_MODULE_ENABLE(0xA5) {
    @Override
    public CommandTranslator createCommandTranslator() {
      return new TwoByteCommandTranslator(this.getOperationCode(), this.getArg());
    }
  },
  CMD_ZM_DISABLE(0xA6) {
    @Override
    public CommandTranslator createCommandTranslator() {
      return new TwoByteCommandTranslator(this.getOperationCode(), this.getArg());
    }
  },
  CMD_ZM_ENABLE(0xA7) {
    @Override
    public CommandTranslator createCommandTranslator() {
      return new TwoByteCommandTranslator(this.getOperationCode(), this.getArg());
    }
  },
  CMD_SOFT_RESET(0xA8) {
    @Override
    public CommandTranslator createCommandTranslator() {
      return new TwoByteCommandTranslator(this.getOperationCode(), this.getArg());
    }
  };

  private final int operation;
  private int arg = 0x00; // default no argument

  Command(final int opCode) {
    this.operation = opCode;
  }

  public int getOperationCode() {
    return operation;
  }

  public int getArg() {
    return arg;
  }

  public Command setArg(int arg) {
    this.arg = arg;
    return this;
  }

  public abstract CommandTranslator createCommandTranslator();
}

package ca.nerret.emu.emulator;

public class Bus {

    EECIV_8061 cpu;

    public Bus()
    {
        // CPU
        cpu.ConnectBus(this);

        //Ram
        // Fake RAM
        byte ram[] = new byte[64 * 1024];

    }

    public byte read(short address, boolean readonly)
    {

        return 0x0;
    }
}
 
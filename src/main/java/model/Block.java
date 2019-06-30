package model;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

public class Block {
    private int blockHash;
    private double sum;
    private String mnemonic;
    private Block previousBlock;

    public Block(Block previousBlock, double sum) {
        this.previousBlock = previousBlock;
        this.sum = sum;

        Object[] contents = { sum, previousBlock };
        this.blockHash = Arrays.hashCode(contents);
    }

    public Block getPreviousBlock() {
        return previousBlock;
    }

    public int getBlockHash() {
        return blockHash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;

        Block that = (Block) o;
        return Objects.equals(this.getPreviousBlock(), that.getPreviousBlock());
    }

    @Override
    public int hashCode() {
        return blockHash;
    }
}
package de.thedodo24.xenrodsystem.common.arango;

public interface WritableGenerator<Writable extends ArangoWritable<KeyType>, KeyType> {
    Writable generate(KeyType key);
}

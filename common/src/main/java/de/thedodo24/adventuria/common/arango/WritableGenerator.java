package de.thedodo24.adventuria.common.arango;

import com.arangodb.entity.KeyType;

public interface WritableGenerator<Writable extends ArangoWritable<KeyType>, KeyType> {
    Writable generate(KeyType key);
}

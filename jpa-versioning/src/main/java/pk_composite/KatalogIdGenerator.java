package pk_composite;

import java.util.concurrent.atomic.AtomicLong;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

public class KatalogIdGenerator extends SequenceStyleGenerator {

    static AtomicLong nextId = new AtomicLong();

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {

        if (object instanceof PkCompositeKatalogEntity entity) {
            if (entity.getId() == null) {
                return super.generate(session, object);
            } else {
                return entity.getId();
            }
        }
        throw new RuntimeException("wrong entity class: " + object.getClass());
    }

}

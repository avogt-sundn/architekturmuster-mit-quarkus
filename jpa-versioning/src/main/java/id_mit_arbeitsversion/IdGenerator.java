package id_mit_arbeitsversion;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

public class IdGenerator extends SequenceStyleGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {

        if (object instanceof UsingIdGenerator entity) {
            if (entity.getId() == null) {
                return super.generate(session, object);
            } else {
                return entity.getId();
            }
        } else
            throw new FehlendesInterfaceException(object);
    }

}

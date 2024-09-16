package project7.common.sharedServices;

public interface RegisterService<T,R> {
    R register(T entity);
}

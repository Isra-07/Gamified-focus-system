package com.thrive.data.repository;

import com.thrive.data.local.DistractionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class DistractionRepositoryImpl_Factory implements Factory<DistractionRepositoryImpl> {
  private final Provider<DistractionDao> daoProvider;

  public DistractionRepositoryImpl_Factory(Provider<DistractionDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public DistractionRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static DistractionRepositoryImpl_Factory create(Provider<DistractionDao> daoProvider) {
    return new DistractionRepositoryImpl_Factory(daoProvider);
  }

  public static DistractionRepositoryImpl newInstance(DistractionDao dao) {
    return new DistractionRepositoryImpl(dao);
  }
}

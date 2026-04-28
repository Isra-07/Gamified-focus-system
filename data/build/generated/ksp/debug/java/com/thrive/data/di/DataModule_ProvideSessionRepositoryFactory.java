package com.thrive.data.di;

import com.thrive.data.repository.SessionRepositoryImpl;
import com.thrive.domain.SessionRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DataModule_ProvideSessionRepositoryFactory implements Factory<SessionRepository> {
  private final Provider<SessionRepositoryImpl> implProvider;

  public DataModule_ProvideSessionRepositoryFactory(Provider<SessionRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public SessionRepository get() {
    return provideSessionRepository(implProvider.get());
  }

  public static DataModule_ProvideSessionRepositoryFactory create(
      Provider<SessionRepositoryImpl> implProvider) {
    return new DataModule_ProvideSessionRepositoryFactory(implProvider);
  }

  public static SessionRepository provideSessionRepository(SessionRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideSessionRepository(impl));
  }
}

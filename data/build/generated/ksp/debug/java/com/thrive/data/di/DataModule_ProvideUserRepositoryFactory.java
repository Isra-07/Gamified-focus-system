package com.thrive.data.di;

import com.thrive.data.repository.UserRepositoryImpl;
import com.thrive.domain.UserRepository;
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
public final class DataModule_ProvideUserRepositoryFactory implements Factory<UserRepository> {
  private final Provider<UserRepositoryImpl> implProvider;

  public DataModule_ProvideUserRepositoryFactory(Provider<UserRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public UserRepository get() {
    return provideUserRepository(implProvider.get());
  }

  public static DataModule_ProvideUserRepositoryFactory create(
      Provider<UserRepositoryImpl> implProvider) {
    return new DataModule_ProvideUserRepositoryFactory(implProvider);
  }

  public static UserRepository provideUserRepository(UserRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(DataModule.INSTANCE.provideUserRepository(impl));
  }
}

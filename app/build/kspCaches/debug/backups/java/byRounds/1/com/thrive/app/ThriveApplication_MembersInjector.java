package com.thrive.app;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class ThriveApplication_MembersInjector implements MembersInjector<ThriveApplication> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public ThriveApplication_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<ThriveApplication> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new ThriveApplication_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(ThriveApplication instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.thrive.app.ThriveApplication.workerFactory")
  public static void injectWorkerFactory(ThriveApplication instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}

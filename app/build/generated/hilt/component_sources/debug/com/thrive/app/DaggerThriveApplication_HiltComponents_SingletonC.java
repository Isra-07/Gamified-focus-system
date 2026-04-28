package com.thrive.app;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import com.thrive.data.di.DataModule_ProvideChallengeRepositoryFactory;
import com.thrive.data.di.DataModule_ProvideDatabaseFactory;
import com.thrive.data.di.DataModule_ProvideDistractionDaoFactory;
import com.thrive.data.di.DataModule_ProvideDistractionRepositoryFactory;
import com.thrive.data.di.DataModule_ProvideSessionDaoFactory;
import com.thrive.data.di.DataModule_ProvideSessionRepositoryFactory;
import com.thrive.data.di.DataModule_ProvideUserDaoFactory;
import com.thrive.data.di.DataModule_ProvideUserRepositoryFactory;
import com.thrive.data.distraction.DistractionDetector;
import com.thrive.data.local.DistractionDao;
import com.thrive.data.local.SessionDao;
import com.thrive.data.local.ThriveDatabase;
import com.thrive.data.local.UserDao;
import com.thrive.data.repository.ChallengeRepositoryImpl;
import com.thrive.data.repository.DistractionRepositoryImpl;
import com.thrive.data.repository.SessionRepositoryImpl;
import com.thrive.data.repository.UserRepositoryImpl;
import com.thrive.domain.DistractionRepository;
import com.thrive.domain.SessionRepository;
import com.thrive.domain.UserRepository;
import com.thrive.domain.repository.ChallengeRepository;
import com.thrive.feature.auth.AuthViewModel;
import com.thrive.feature.auth.AuthViewModel_HiltModules;
import com.thrive.feature.challenge.ChallengeDetailViewModel;
import com.thrive.feature.challenge.ChallengeDetailViewModel_HiltModules;
import com.thrive.feature.challenge.ChallengeViewModel;
import com.thrive.feature.challenge.ChallengeViewModel_HiltModules;
import com.thrive.feature.home.HomeViewModel;
import com.thrive.feature.home.HomeViewModel_HiltModules;
import com.thrive.feature.home.ProfileViewModel;
import com.thrive.feature.home.ProfileViewModel_HiltModules;
import com.thrive.feature.summary.AnalyticsViewModel;
import com.thrive.feature.summary.AnalyticsViewModel_HiltModules;
import com.thrive.feature.summary.LeaderboardViewModel;
import com.thrive.feature.summary.LeaderboardViewModel_HiltModules;
import com.thrive.feature.timer.TimerViewModel;
import com.thrive.feature.timer.TimerViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerThriveApplication_HiltComponents_SingletonC {
  private DaggerThriveApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public ThriveApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements ThriveApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public ThriveApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements ThriveApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public ThriveApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements ThriveApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public ThriveApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements ThriveApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ThriveApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements ThriveApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public ThriveApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements ThriveApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public ThriveApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements ThriveApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public ThriveApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends ThriveApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends ThriveApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends ThriveApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends ThriveApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity arg0) {
    }

    @Override
    public void injectSplashActivity(SplashActivity arg0) {
      injectSplashActivity2(arg0);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(8).put(LazyClassKeyProvider.com_thrive_feature_summary_AnalyticsViewModel, AnalyticsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_thrive_feature_auth_AuthViewModel, AuthViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_thrive_feature_challenge_ChallengeDetailViewModel, ChallengeDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_thrive_feature_challenge_ChallengeViewModel, ChallengeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_thrive_feature_home_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_thrive_feature_summary_LeaderboardViewModel, LeaderboardViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_thrive_feature_home_ProfileViewModel, ProfileViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_thrive_feature_timer_TimerViewModel, TimerViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private SplashActivity injectSplashActivity2(SplashActivity instance) {
      SplashActivity_MembersInjector.injectUserRepository(instance, singletonCImpl.provideUserRepositoryProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_thrive_feature_timer_TimerViewModel = "com.thrive.feature.timer.TimerViewModel";

      static String com_thrive_feature_challenge_ChallengeDetailViewModel = "com.thrive.feature.challenge.ChallengeDetailViewModel";

      static String com_thrive_feature_home_ProfileViewModel = "com.thrive.feature.home.ProfileViewModel";

      static String com_thrive_feature_home_HomeViewModel = "com.thrive.feature.home.HomeViewModel";

      static String com_thrive_feature_auth_AuthViewModel = "com.thrive.feature.auth.AuthViewModel";

      static String com_thrive_feature_challenge_ChallengeViewModel = "com.thrive.feature.challenge.ChallengeViewModel";

      static String com_thrive_feature_summary_AnalyticsViewModel = "com.thrive.feature.summary.AnalyticsViewModel";

      static String com_thrive_feature_summary_LeaderboardViewModel = "com.thrive.feature.summary.LeaderboardViewModel";

      @KeepFieldType
      TimerViewModel com_thrive_feature_timer_TimerViewModel2;

      @KeepFieldType
      ChallengeDetailViewModel com_thrive_feature_challenge_ChallengeDetailViewModel2;

      @KeepFieldType
      ProfileViewModel com_thrive_feature_home_ProfileViewModel2;

      @KeepFieldType
      HomeViewModel com_thrive_feature_home_HomeViewModel2;

      @KeepFieldType
      AuthViewModel com_thrive_feature_auth_AuthViewModel2;

      @KeepFieldType
      ChallengeViewModel com_thrive_feature_challenge_ChallengeViewModel2;

      @KeepFieldType
      AnalyticsViewModel com_thrive_feature_summary_AnalyticsViewModel2;

      @KeepFieldType
      LeaderboardViewModel com_thrive_feature_summary_LeaderboardViewModel2;
    }
  }

  private static final class ViewModelCImpl extends ThriveApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AnalyticsViewModel> analyticsViewModelProvider;

    private Provider<AuthViewModel> authViewModelProvider;

    private Provider<ChallengeDetailViewModel> challengeDetailViewModelProvider;

    private Provider<ChallengeViewModel> challengeViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<LeaderboardViewModel> leaderboardViewModelProvider;

    private Provider<ProfileViewModel> profileViewModelProvider;

    private Provider<TimerViewModel> timerViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.analyticsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.authViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.challengeDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.challengeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.leaderboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.profileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.timerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(8).put(LazyClassKeyProvider.com_thrive_feature_summary_AnalyticsViewModel, ((Provider) analyticsViewModelProvider)).put(LazyClassKeyProvider.com_thrive_feature_auth_AuthViewModel, ((Provider) authViewModelProvider)).put(LazyClassKeyProvider.com_thrive_feature_challenge_ChallengeDetailViewModel, ((Provider) challengeDetailViewModelProvider)).put(LazyClassKeyProvider.com_thrive_feature_challenge_ChallengeViewModel, ((Provider) challengeViewModelProvider)).put(LazyClassKeyProvider.com_thrive_feature_home_HomeViewModel, ((Provider) homeViewModelProvider)).put(LazyClassKeyProvider.com_thrive_feature_summary_LeaderboardViewModel, ((Provider) leaderboardViewModelProvider)).put(LazyClassKeyProvider.com_thrive_feature_home_ProfileViewModel, ((Provider) profileViewModelProvider)).put(LazyClassKeyProvider.com_thrive_feature_timer_TimerViewModel, ((Provider) timerViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_thrive_feature_challenge_ChallengeViewModel = "com.thrive.feature.challenge.ChallengeViewModel";

      static String com_thrive_feature_summary_AnalyticsViewModel = "com.thrive.feature.summary.AnalyticsViewModel";

      static String com_thrive_feature_challenge_ChallengeDetailViewModel = "com.thrive.feature.challenge.ChallengeDetailViewModel";

      static String com_thrive_feature_home_HomeViewModel = "com.thrive.feature.home.HomeViewModel";

      static String com_thrive_feature_auth_AuthViewModel = "com.thrive.feature.auth.AuthViewModel";

      static String com_thrive_feature_timer_TimerViewModel = "com.thrive.feature.timer.TimerViewModel";

      static String com_thrive_feature_summary_LeaderboardViewModel = "com.thrive.feature.summary.LeaderboardViewModel";

      static String com_thrive_feature_home_ProfileViewModel = "com.thrive.feature.home.ProfileViewModel";

      @KeepFieldType
      ChallengeViewModel com_thrive_feature_challenge_ChallengeViewModel2;

      @KeepFieldType
      AnalyticsViewModel com_thrive_feature_summary_AnalyticsViewModel2;

      @KeepFieldType
      ChallengeDetailViewModel com_thrive_feature_challenge_ChallengeDetailViewModel2;

      @KeepFieldType
      HomeViewModel com_thrive_feature_home_HomeViewModel2;

      @KeepFieldType
      AuthViewModel com_thrive_feature_auth_AuthViewModel2;

      @KeepFieldType
      TimerViewModel com_thrive_feature_timer_TimerViewModel2;

      @KeepFieldType
      LeaderboardViewModel com_thrive_feature_summary_LeaderboardViewModel2;

      @KeepFieldType
      ProfileViewModel com_thrive_feature_home_ProfileViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.thrive.feature.summary.AnalyticsViewModel 
          return (T) new AnalyticsViewModel(singletonCImpl.provideSessionRepositoryProvider.get(), singletonCImpl.provideUserRepositoryProvider.get());

          case 1: // com.thrive.feature.auth.AuthViewModel 
          return (T) new AuthViewModel(singletonCImpl.provideUserRepositoryProvider.get());

          case 2: // com.thrive.feature.challenge.ChallengeDetailViewModel 
          return (T) new ChallengeDetailViewModel(singletonCImpl.provideChallengeRepositoryProvider.get(), singletonCImpl.provideUserRepositoryProvider.get());

          case 3: // com.thrive.feature.challenge.ChallengeViewModel 
          return (T) new ChallengeViewModel(singletonCImpl.provideChallengeRepositoryProvider.get(), singletonCImpl.provideUserRepositoryProvider.get());

          case 4: // com.thrive.feature.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.provideUserRepositoryProvider.get(), singletonCImpl.provideSessionRepositoryProvider.get(), singletonCImpl.provideChallengeRepositoryProvider.get());

          case 5: // com.thrive.feature.summary.LeaderboardViewModel 
          return (T) new LeaderboardViewModel(singletonCImpl.provideUserRepositoryProvider.get());

          case 6: // com.thrive.feature.home.ProfileViewModel 
          return (T) new ProfileViewModel(singletonCImpl.provideUserRepositoryProvider.get());

          case 7: // com.thrive.feature.timer.TimerViewModel 
          return (T) new TimerViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideSessionRepositoryProvider.get(), singletonCImpl.provideUserRepositoryProvider.get(), singletonCImpl.provideDistractionRepositoryProvider.get(), singletonCImpl.distractionDetectorProvider.get(), singletonCImpl.provideChallengeRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends ThriveApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends ThriveApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends ThriveApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<ThriveDatabase> provideDatabaseProvider;

    private Provider<UserDao> provideUserDaoProvider;

    private Provider<UserRepositoryImpl> userRepositoryImplProvider;

    private Provider<UserRepository> provideUserRepositoryProvider;

    private Provider<SessionDao> provideSessionDaoProvider;

    private Provider<SessionRepositoryImpl> sessionRepositoryImplProvider;

    private Provider<SessionRepository> provideSessionRepositoryProvider;

    private Provider<ChallengeRepositoryImpl> challengeRepositoryImplProvider;

    private Provider<ChallengeRepository> provideChallengeRepositoryProvider;

    private Provider<DistractionDao> provideDistractionDaoProvider;

    private Provider<DistractionRepositoryImpl> distractionRepositoryImplProvider;

    private Provider<DistractionRepository> provideDistractionRepositoryProvider;

    private Provider<DistractionDetector> distractionDetectorProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(Collections.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>emptyMap());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<ThriveDatabase>(singletonCImpl, 3));
      this.provideUserDaoProvider = DoubleCheck.provider(new SwitchingProvider<UserDao>(singletonCImpl, 2));
      this.userRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<UserRepositoryImpl>(singletonCImpl, 1));
      this.provideUserRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<UserRepository>(singletonCImpl, 0));
      this.provideSessionDaoProvider = DoubleCheck.provider(new SwitchingProvider<SessionDao>(singletonCImpl, 6));
      this.sessionRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<SessionRepositoryImpl>(singletonCImpl, 5));
      this.provideSessionRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SessionRepository>(singletonCImpl, 4));
      this.challengeRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<ChallengeRepositoryImpl>(singletonCImpl, 8));
      this.provideChallengeRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<ChallengeRepository>(singletonCImpl, 7));
      this.provideDistractionDaoProvider = DoubleCheck.provider(new SwitchingProvider<DistractionDao>(singletonCImpl, 11));
      this.distractionRepositoryImplProvider = DoubleCheck.provider(new SwitchingProvider<DistractionRepositoryImpl>(singletonCImpl, 10));
      this.provideDistractionRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<DistractionRepository>(singletonCImpl, 9));
      this.distractionDetectorProvider = DoubleCheck.provider(new SwitchingProvider<DistractionDetector>(singletonCImpl, 12));
    }

    @Override
    public void injectThriveApplication(ThriveApplication thriveApplication) {
      injectThriveApplication2(thriveApplication);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private ThriveApplication injectThriveApplication2(ThriveApplication instance) {
      ThriveApplication_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.thrive.domain.UserRepository 
          return (T) DataModule_ProvideUserRepositoryFactory.provideUserRepository(singletonCImpl.userRepositoryImplProvider.get());

          case 1: // com.thrive.data.repository.UserRepositoryImpl 
          return (T) new UserRepositoryImpl(singletonCImpl.provideUserDaoProvider.get());

          case 2: // com.thrive.data.local.UserDao 
          return (T) DataModule_ProvideUserDaoFactory.provideUserDao(singletonCImpl.provideDatabaseProvider.get());

          case 3: // com.thrive.data.local.ThriveDatabase 
          return (T) DataModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.thrive.domain.SessionRepository 
          return (T) DataModule_ProvideSessionRepositoryFactory.provideSessionRepository(singletonCImpl.sessionRepositoryImplProvider.get());

          case 5: // com.thrive.data.repository.SessionRepositoryImpl 
          return (T) new SessionRepositoryImpl(singletonCImpl.provideSessionDaoProvider.get());

          case 6: // com.thrive.data.local.SessionDao 
          return (T) DataModule_ProvideSessionDaoFactory.provideSessionDao(singletonCImpl.provideDatabaseProvider.get());

          case 7: // com.thrive.domain.repository.ChallengeRepository 
          return (T) DataModule_ProvideChallengeRepositoryFactory.provideChallengeRepository(singletonCImpl.challengeRepositoryImplProvider.get());

          case 8: // com.thrive.data.repository.ChallengeRepositoryImpl 
          return (T) new ChallengeRepositoryImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 9: // com.thrive.domain.DistractionRepository 
          return (T) DataModule_ProvideDistractionRepositoryFactory.provideDistractionRepository(singletonCImpl.distractionRepositoryImplProvider.get());

          case 10: // com.thrive.data.repository.DistractionRepositoryImpl 
          return (T) new DistractionRepositoryImpl(singletonCImpl.provideDistractionDaoProvider.get());

          case 11: // com.thrive.data.local.DistractionDao 
          return (T) DataModule_ProvideDistractionDaoFactory.provideDistractionDao(singletonCImpl.provideDatabaseProvider.get());

          case 12: // com.thrive.data.distraction.DistractionDetector 
          return (T) new DistractionDetector(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}

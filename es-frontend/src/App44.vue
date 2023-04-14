<template>
  <app-header></app-header>
  <app-nav-bar></app-nav-bar>
  <router-view></router-view>
</template>

<script>
import { SessionSbService } from '@/SessionSbService'
import { reactive, shallowReactive } from 'vue'
import Header from '@/components/Header.vue'
import NavBar from '@/components/NavBar.vue'
import { ScooterAdaptor } from '@/models/ScooterAdaptor'
import CONFIG from '@/app-config'
import HeaderSb from '@/components/HeaderSb'
import NavBarSb from '@/components/NavBarSb'
import { FetchInterceptor } from '@/FetchInterceptor'
import { TripAdaptor } from '@/models/TripAdaptor'

export default {
  name: 'App44',
  components: {
    // eslint-disable-next-line vue/no-unused-components
    'app-header': HeaderSb,
    // eslint-disable-next-line vue/no-unused-components
    'app-nav-bar': NavBarSb
  },
  provide () {
    this.theSessionService = shallowReactive(
      new SessionSbService(CONFIG.BACKEND_URL + '/authentication', CONFIG.JWT_STORAGE_ITEM)
    )
    this.theFetchInterceptor =
      new FetchInterceptor(this.theSessionService, this.$router)

    return {
      scootersService: new ScooterAdaptor(CONFIG.BACKEND_URL + '/scooters'),
      tripsService: new TripAdaptor(CONFIG.BACKEND_URL + '/trips'),
      // tripsService: new RESTAdaptorInterface(CONFIG.BACKEND_URL + '/trips'),
      sessionService: this.theSessionService
    }
  },
  unmounted () {
    console.log('App.unmounted() has been called')
    this.theFetchInterceptor.unregister()
  }

}
</script>

<style scoped>

</style>

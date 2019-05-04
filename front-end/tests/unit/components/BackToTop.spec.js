import { shallowMount } from '@vue/test-utils';
import BackToTop from '@/components/BackToTop/index.vue';

test('is a Vue instance', () => {
  const wrapper = shallowMount(BackToTop)
  expect(wrapper.isVueInstance()).toBeTruthy()
})

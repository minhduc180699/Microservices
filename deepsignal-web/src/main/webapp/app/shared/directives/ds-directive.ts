export function initDirective(vue) {
  const LOADING_HTML =
    '<div class="loading-center"><div class="spinner-border" role="status">' + '  <span class="sr-only">Loading...</span>' + '</div></div>';
  const _uid = 'vue-directive-loading' + Date.now().toString(16);

  vue.directive('loading', {
    inserted(el, binding, vnode) {
      const spinner = document.createElement('span');
      spinner.id = _uid;
      spinner.innerHTML = LOADING_HTML;
      spinner.style['display'] = binding.value ? 'block' : 'none';
      spinner.style.width = '-webkit-fill-available';
      spinner.style.height = '-webkit-fill-available';
      spinner.className = 'loading-absolute';
      // el.childNodes.forEach((item) => {
      //   console.log('item  =', item);
      //   item.style.display = binding.value ? 'none' : ''
      // });
      el.appendChild(spinner);
    },
    update(el, binding, vnode) {
      const spinner = document.getElementById(_uid);
      spinner.style.display = binding.value ? 'block' : 'none';
      // el.childNodes.forEach((item) => {
      //   if(item.id === _uid) {
      //     return item.style.display = binding.value ? 'none' : '';
      //   }
      // })
    },
  });

  vue.directive('autotrim', {
    inserted(el) {
      if (!(el instanceof HTMLInputElement) && !(el instanceof HTMLTextAreaElement)) {
        throw 'Cannot apply v-autotrim directive to a non-input element!';
      }
      el.addEventListener('blur', () => {
        if (el.value) {
          el.value = String.prototype.trimAllSpace(el.value);
          el.dispatchEvent(new Event('input'));
        }
      });
    },
  });

  vue.directive('click-outside', {
    bind(el, binding, vnode) {
      el.clickOutsideEvent = function (event) {
        // here I check that click was outside the el and his children
        if (!(el == event.target || el.contains(event.target))) {
          // and if it did, call method provided in attribute value
          vnode.context[binding.expression](event);
        }
      };
      document.body.addEventListener('click', el.clickOutsideEvent);
    },
    unbind(el) {
      document.body.removeEventListener('click', el.clickOutsideEvent);
    },
  });

  vue.directive('ds-skeleton', {
    inserted(el, binding, vnode) {
      console.log('called to ds-skeleton', el, binding);
    },
    update(el, binding, vnode) {
      console.log('called to updated', el);
    },
  });

  vue.directive('stop-propagation', {
    bind(el) {
      el.onclick = function (event) {
        event.stopPropagation();
      };
    },
  });
}

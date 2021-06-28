// state
const state = {
  set: function (target, key, value) {
    target[key] = value
    handleStateChange()
    return true
  }
}

const store = new Proxy({
  password: '',
  passwordConfirm: '',
  passwordConfirmHasBeenTouched: false,
  passwordHasBeenTouched: false,
}, state)

// methods
function handlePasswordConfirmInput (event) {
  store.passwordConfirm = event.target.value
}

function handlePasswordInput (event) {
  store.password = event.target.value
}

function handleRequiredField (event) {
  const field = event.target.id

  if (field === 'password-confirm') {
    store.passwordConfirmHasBeenTouched = true
  }

  if (field === 'password') {
    store.passwordHasBeenTouched = true
  }

  const label = event.target.parentElement

  if (!event.target.value) {
    label.setAttribute('data-error', 'Precisa inserir esse dado')
    label.classList.add('error', 'required-error')
  } else {
    label.removeAttribute('data-error', 'Precisa inserir esse dado')
    label.classList.remove('error', 'required-error')
  }
}

function validate (formField) {
  if (
    store.passwordConfirmHasBeenTouched &&
    store.passwordHasBeenTouched &&
    store.password &&
    store.passwordConfirm
  ) {
    if (store.passwordConfirm !== store.password) {
      formField.setAttribute('data-error', 'As senhas não coincidem')
      formField.classList.add('error')
    }
  }
}

// state observer
function handleStateChange () {
  const button = document.querySelector('#set-new-password')
  if (store.password === store.passwordConfirm) {
    button.removeAttribute('disabled')
  } else {
    button.setAttribute('disabled', true)
  }
}

// event listeners
window.addEventListener('load', () => {
  const pass = document.querySelector('#password')
  const confirm = document.querySelector('#password-confirm')

  pass.addEventListener('input', handlePasswordInput)
  confirm.addEventListener('input', handlePasswordConfirmInput)

  pass.addEventListener('blur', e => {
    handleRequiredField(e)
    validate(pass.parentElement)
  })

  confirm.addEventListener('input', e => {
    handleRequiredField(e)
    validate(confirm.parentElement)
  })
});

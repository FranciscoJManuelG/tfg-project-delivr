import React, {useState} from 'react';
import {useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useHistory} from 'react-router-dom';
import {Link} from 'react-router-dom';

import {Errors} from '../../common';
import * as actions from '../actions';

const SignUp = () => {

    const dispatch = useDispatch();
    const history = useHistory();
    const [userName, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail]  = useState('');
    const [phone, setPhone]  = useState('');
    const [backendErrors, setBackendErrors] = useState(null);
    const [passwordsDoNotMatch, setPasswordsDoNotMatch] = useState(false);
    let form;
    let confirmPasswordInput;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity() && checkConfirmPassword()) {
            
            dispatch(actions.signUp(
                {userName: userName.trim(),
                password: password,
                firstName: firstName.trim(),
                lastName: lastName.trim(),
                email: email.trim(),
                phone: phone.trim()},
                () => history.push('/'),
                errors => setBackendErrors(errors),
                () => {
                    history.push('/users/login');
                    dispatch(actions.logout());
                }
            ));
            

        } else {

            setBackendErrors(null);
            form.classList.add('was-validated');

        }

    }

    const checkConfirmPassword = () => {

        if (password !== confirmPassword) {

            confirmPasswordInput.setCustomValidity('error');
            setPasswordsDoNotMatch(true);

            return false;

        } else {
            return true;
        }

    }

    const handleConfirmPasswordChange = value => {

        confirmPasswordInput.setCustomValidity('');
        setConfirmPassword(value);
        setPasswordsDoNotMatch(false);
    
    }

    return (
        <div class="container">
            <div class="row justify-content-center">
                <aside class="col-sm-6">
                    <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                    <p class="text-center">
                        <Link to="/users/signup-businessman">
                            <FormattedMessage id="project.users.SignUpCompany.title"/>
                        </Link>
                    </p>
                    <div class="card">
                        <article class="card-body">
                	        <a href="/users/login" class="float-right btn btn-outline-primary">Inicia sesión</a>
                	        <h4 class="card-title mb-4 mt-1">Regístrate</h4>
                	        <br/>
                            <form ref={node => form = node} class="needs-validation" 
                                noValidate onSubmit={e => handleSubmit(e)}>
                                <div class="form-group">
                                    <input type="text" id="userName" class="form-control" 
                                        placeholder="Username"
                                        value={userName}
                                        onChange={e => setUserName(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div> 
                                <div class="form-group">
                                    <input type="password" id="password" class="form-control" 
                                        placeholder="Contraseña"
                                        value={password}
                                        onChange={e => setPassword(e.target.value)}
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <input ref={node => confirmPasswordInput = node}
                                        type="password" id="confirmPassword" className="form-control"
                                        placeholder="Repita la contraseña"
                                        value={confirmPassword}
                                        onChange={e => handleConfirmPasswordChange(e.target.value)}
                                        required/>
                                    <div className="invalid-feedback">
                                        {passwordsDoNotMatch ?
                                            <FormattedMessage id='project.global.validator.passwordsDoNotMatch'/> :
                                            <FormattedMessage id='project.global.validator.required'/>}
                                    </div>
                                </div>
                                <div class="form-group">
                                    <input type="text" id="firstName" className="form-control"
                                        placeholder="Nombre completo"
                                        value={firstName}
                                        onChange={e => setFirstName(e.target.value)}
                                        required/>
                                    <div class="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <input type="text" id="lastName" class="form-control"
                                        placeholder="Apellidos"
                                        value={lastName}
                                        onChange={e => setLastName(e.target.value)}
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <input type="email" id="email" class="form-control"
                                        placeholder="Email"
                                        value={email}
                                        onChange={e => setEmail(e.target.value)}
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.email'/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <input type="text" id="phone" class="form-control"
                                        placeholder="Teléfono"
                                        value={phone}
                                        onChange={e => setPhone(e.target.value)}
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>     
                                <br/>
                                <div class="row">
                                    <div class="col">
                                        <div class="form-group">
                                            <button type="submit" class="btn btn-primary btn-block">Regístrate</button>
                                        </div> 
                                    </div>
                                </div>                                                              
                            </form>
                        </article>
                    </div>
	            </aside> 
            </div>
        </div> 
    );

}

export default SignUp;
import React, {useState} from 'react';
import {useDispatch} from 'react-redux';
import {Link} from 'react-router-dom';
import {FormattedMessage} from 'react-intl';
import {useHistory} from 'react-router-dom';

import {Errors} from '../../common';
import * as actions from '../actions';

const Login = () => {

    const dispatch = useDispatch();
    const history = useHistory();
    const [userName, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [backendErrors, setBackendErrors] = useState(null);
    let form;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {

            dispatch(actions.login(
                userName.trim(),
                password,
                () => history.push('/users/check-pay-fee'),
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

    return (
        <div className="container">
            <div className="row justify-content-center">
                <aside className="col-sm-4">
                    <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                    <p className="text-center">
                        <Link to="/users/signup-businessman">
                            <FormattedMessage id="project.users.SignUpCompany.title"/>
                        </Link>
                    </p>
                    <div className="card">
                        <article className="card-body">
                	        <a href="/users/signup" className="float-right btn btn-outline-primary">Regístrate</a>
                	        <h4 className="card-title mb-4 mt-1">Inicia sesión</h4>
                	        <br/>
                            <form ref={node => form = node} className="needs-validation" 
                                noValidate onSubmit={e => handleSubmit(e)}>
                                <div className="form-group">
                                    <input type="text" id="userName" className="form-control" 
                                        placeholder="Introduce tu username"
                                        value={userName}
                                        onChange={e => setUserName(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div> 
                                <div className="form-group">
                                    <input type="password" id="password" className="form-control" 
                                        placeholder="Introduce tu contraseña"
                                        value={password}
                                        onChange={e => setPassword(e.target.value)}
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>    
                                <br/>
                                <div className="row">
                                    <div className="col">
                                        <div className="form-group">
                                            <button type="submit" className="btn btn-primary btn-block">Inicia sesión</button>
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

export default Login;
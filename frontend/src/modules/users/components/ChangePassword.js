import React, {useState} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useHistory} from 'react-router-dom';

import {Errors} from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';

const ChangePassword = () => {

    const user = useSelector(selectors.getUser);
    const dispatch = useDispatch();
    const history = useHistory();
    const [oldPassword, setOldPassword] = useState(user.oldPassword);
    const [newPassword, setNewPassword] = useState('');
    const [confirmNewPassword, setConfirmNewPassword] = useState('');
    const [backendErrors, setBackendErrors] = useState(null);
    const [passwordsDoNotMatch, setPasswordsDoNotMatch] = useState(false);
    let form;
    let confirmNewPasswordInput;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity() && checkConfirmNewPassword()) {

            dispatch(actions.changePassword(user.id, oldPassword, newPassword,
                () => history.push('/'),
                errors => setBackendErrors(errors)));

        } else {

            setBackendErrors(null);
            form.classList.add('was-validated');
            
        }

    }

    const checkConfirmNewPassword = () => {

        if (newPassword !== confirmNewPassword) {

            confirmNewPasswordInput.setCustomValidity('error');
            setPasswordsDoNotMatch(true);

            return false;

        } else {
            return true;
        }

    }

    const handleConfirmNewPasswordChange = event => {

        confirmNewPasswordInput.setCustomValidity('');
        setConfirmNewPassword(event.target.value);
        setPasswordsDoNotMatch(false);

    }

    return (
        <div class="container">
            <div class="row justify-content-center">
                <aside class="col-sm-5">
                    <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                    <div class="card">
                        <article class="card-body">
                	        <h4 class="card-title mb-4 mt-1">
                                <FormattedMessage id="project.users.ChangePassword.title"/>
                            </h4>
                	        <br/>
                            <form ref={node => form = node} class="needs-validation" 
                                noValidate onSubmit={e => handleSubmit(e)}>
                                <label htmlFor="oldPassword" className="col-form-label">
                                    <FormattedMessage id="project.users.ChangePassword.fields.oldPassword"/>
                                </label>
                                <div class="form-group">
                                    <input type="password" id="oldPassword" className="form-control"   
                                        placeholder ="******"        
                                        value={oldPassword}
                                        onChange={e => setOldPassword(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div> 
                                <label htmlFor="newPassword" className="col-form-label">
                                    <FormattedMessage id="project.users.ChangePassword.fields.newPassword"/>
                                </label>
                                <div class="form-group">
                                    <input type="password" id="newPassword" className="form-control"
                                        placeholder ="******"
                                        value={newPassword}
                                        onChange={e => setNewPassword(e.target.value)}
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <label htmlFor="confirmNewPassword" className="col-form-label">
                                    <FormattedMessage id="project.users.ChangePassword.fields.confirmNewPassword"/>
                                </label>
                                <div class="form-group">
                                    <input ref={node => confirmNewPasswordInput = node}
                                        type="password" id="confirmNewPassword" className="form-control"
                                        placeholder ="******"
                                        value={confirmNewPassword}
                                        onChange={e => handleConfirmNewPasswordChange(e)}
                                        required/>
                                    <div className="invalid-feedback">
                                        {passwordsDoNotMatch ?
                                            <FormattedMessage id='project.global.validator.passwordsDoNotMatch'/> :
                                            <FormattedMessage id='project.global.validator.required'/>}
                                        
                                    </div>
                                </div>    
                                <br/>
                                <div class="row">
                                    <div class="col">
                                        <div class="form-group">
                                            <button type="submit" class="btn btn-primary btn-block">
                                                <FormattedMessage id="project.global.buttons.save"/>
                                            </button>
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

export default ChangePassword;

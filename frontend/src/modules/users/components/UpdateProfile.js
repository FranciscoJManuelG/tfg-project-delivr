import React, {useState} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useHistory} from 'react-router-dom';

import {Errors} from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';

const UpdateProfile = () => {

    const user = useSelector(selectors.getUser);
    const dispatch = useDispatch();
    const history = useHistory();
    const [firstName, setFirstName] = useState(user.firstName);
    const [lastName, setLastName] = useState(user.lastName);
    const [email, setEmail]  = useState(user.email);
    const [phone, setPhone]  = useState(user.phone);
    const [backendErrors, setBackendErrors] = useState(null);
    let form;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {
            
            dispatch(actions.updateProfile(
                {id: user.id,
                firstName: firstName.trim(),
                lastName: lastName.trim(),
                email: email.trim(),
                phone: phone.trim()},
                () => history.push('/'),
                errors => setBackendErrors(errors)));

        } else {

            setBackendErrors(null);
            form.classList.add('was-validated');

        }

    }

    return (
        <div class="container">
            <div class="row justify-content-center">
                <aside class="col-sm-5">
                    <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                    <div class="card">
                        <article class="card-body">
                	        <h4 class="card-title mb-4 mt-1">
                                <FormattedMessage id="project.users.UpdateProfile.title"/>
                            </h4>
                	        <br/>
                            <form ref={node => form = node} class="needs-validation" 
                                noValidate onSubmit={e => handleSubmit(e)}>
                                <label htmlFor="firstName" className="col-form-label">
                                    <FormattedMessage id="project.global.fields.firstName"/>
                                </label>
                                <div class="form-group">
                                    <input type="text" id="firstName" className="form-control"
                                        value={firstName}
                                        onChange={e => setFirstName(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div> 
                                <label htmlFor="lastName" className="col-form-label">
                                    <FormattedMessage id="project.global.fields.lastName"/>
                                </label>
                                <div class="form-group">
                                    <input type="text" id="lastName" className="form-control"
                                        value={lastName}
                                        onChange={e => setLastName(e.target.value)}
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <label htmlFor="email" className="col-form-label">
                                    <FormattedMessage id="project.global.fields.email"/>
                                </label>
                                <div class="form-group">
                                    <input type="email" id="email" className="form-control"
                                        value={email}
                                        onChange={e => setEmail(e.target.value)}
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.email'/>
                                    </div>
                                </div>    
                                <label htmlFor="phone" className="col-form-label">
                                    <FormattedMessage id="project.global.fields.phone"/>
                                </label>
                                <div class="form-group">
                                    <input type="tel" id="phone" className="form-control"
                                        value={phone}
                                        onChange={e => setPhone(e.target.value)}
                                        pattern="[0-9]{9}"
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
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

export default UpdateProfile;
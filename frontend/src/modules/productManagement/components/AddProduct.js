import React, {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useHistory} from 'react-router-dom';

import ProductCategorySelector from './ProductCategorySelector';
import * as businessSelectors from '../../business/selectors'
import {Errors} from '../../common';
import Cancel from '../../common/components/Cancel';
import * as actions from '../actions';

const AddProduct = () => {

    const company = useSelector(businessSelectors.getCompany);
    const dispatch = useDispatch();
    const history = useHistory();
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');
    const [file, setFile] = useState(null);
    const [productCategoryId, setProductCategoryId]  = useState(1);
    const [backendErrors, setBackendErrors] = useState(null);
    let form;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {
            
            dispatch(actions.addProduct(company.id,
                name.trim(), description.trim(),
                price, file, productCategoryId,
                () => history.push('/user/login'),
                errors => setBackendErrors(errors)));

        } else {

            setBackendErrors(null);
            form.classList.add('was-validated');

        }
    }

    const toNumber = value => value.length > 0 ? Number(value) : null;

    return (
        <div className="container">
            <div className="row justify-content-center" >
                <aside className="col-sm-6">
                    <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
                    <div className="card">
                        <article className="card-body">
                            <form ref={node => form = node} className="needs-validation" 
                                noValidate onSubmit={e => handleSubmit(e)} encType="multipart/form-data">
                                <div className="form-group">
                                    <label htmlFor="name">Nombre: </label>
                                    <input type="text" id="name" className="form-control" 
                                        value={name}
                                        onChange={e => setName(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="description">Descripción (ingredientes, productos de un menú o información adicional): </label>
                                    <input type="text" id="description" className="form-control" 
                                        value={description}
                                        onChange={e => setDescription(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div> 
                                <div className="form-group">
                                    <label htmlFor="productCategoryId">Categoría: </label>
                                    <ProductCategorySelector id="productCategoryId" className="custom-select my-1 mr-sm-2"
                                    value={productCategoryId} 
                                    onChange={e => setProductCategoryId(e.target.value)}/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="price">Precio: </label>
                                    <input type="number" id="price" min="0.01" step ="0.01" className="form-control" 
                                        value={price}
                                        onChange={e => setPrice(e.target.value)}
                                        autoFocus
                                        required/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="file">Adjunta una imagen: </label>
                                    <input type="file" id="file" className="form-control" accept="image/*"
                                        onChange={e => setFile(e.target.files[0])}/>
                                    <div className="invalid-feedback">
                                        <FormattedMessage id='project.global.validator.required'/>
                                    </div>
                                </div>
                                <br/>
                                <div className="row">
                                    <div className="col">
                                        <Cancel/>
                                        <div className="form-group">
                                            <button type="submit" className="btn btn-primary float-right">Guardar</button>
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

export default AddProduct;
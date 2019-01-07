package jp.co.run.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.run.repository.UserRepository;
import jp.co.run.service.UserService;
import jp.co.run.service.dto.UserSubDto;
import jp.co.run.web.rest.errors.SelectException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserSubDto getUserByUsername(String username) {

        try {
            Optional<UserSubDto> optional = userRepository.findOneByUsername(username);
            if (optional.isPresent()) {
                return optional.get();
            }
        } catch (Exception e) {
            throw new SelectException();
        }

        return null;
    }

}

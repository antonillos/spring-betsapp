package com.betsapp.bets.services;

import com.betsapp.bets.domain.UserBet;
import com.betsapp.bets.exceptions.NotFoundException;
import com.betsapp.bets.mappers.UserBetMapper;
import com.betsapp.bets.repositories.UserBetRepository;
import com.betsapp.mgr.domain.Match;
import com.betsapp.mgr.repositories.MatchRepository;
import com.betsapp.usr.domain.User;
import com.betsapp.usr.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserBetService {

    //@Autowired
    private UserBetMapper mapper;

    //@Autowired
    private UserBetRepository userBetRepository;

    //@Autowired
    private UserRepository userRepository;

    //@Autowired
    private MatchRepository matchRepository;

    @Autowired
    public UserBetService(UserBetMapper mapper, UserBetRepository userBetRepository, UserRepository userRepository, MatchRepository matchRepository) {
        this.mapper = mapper;
        this.userBetRepository = userBetRepository;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
    }

    @Transactional(readOnly=true)
    public List<UserBetDTO> findAll() {
        return mapper.toDto(userBetRepository.findAll());
    }

    @Transactional(readOnly=true)
    public List<UserBetDTO> findAllByUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        user.orElseThrow(() -> new NotFoundException("User not found"));

        UserBet criteria = new UserBet();
        criteria.setUser(user.get());

        Example<UserBet> example = Example.of(criteria);

        return mapper.toDto(userBetRepository.findAll(example));
    }

    @Transactional
    public UserBetDTO findByIdAndUser(Long userId, Long id) {
        Optional<User> user = userRepository.findById(userId);
        user.orElseThrow(() -> new NotFoundException("User not found"));

        Optional<UserBet> ub = userBetRepository.findById(id);
        ub.orElseThrow(() -> new NotFoundException("Bet not found!"));

        return mapper.toDto(userBetRepository.findByIdAndUserId(id, userId).get());
    }

    @Transactional
    public UserBetDTO create(UserBetDTO userBet) {

        Optional<User> user = userRepository.findById(userBet.getUser());
        user.orElseThrow(() -> new NotFoundException("User not found"));

        Optional<Match> match = matchRepository.findByIdOptional(userBet.getMatch());
        match.orElseThrow(() -> new NotFoundException("Match not found"));

        UserBet newUserBet = mapper.toEntity(userBet);
        newUserBet.setCreated(LocalDateTime.now());

        return mapper.toDto(userBetRepository.save(newUserBet));
    }

    @Transactional
    public UserBetDTO findById(Long id) {
        Optional<UserBet> userBet = userBetRepository.findById(id);
        userBet.orElseThrow(() -> new NotFoundException("UserBet not found"));
        return mapper.toDto(userBet.get());
    }
}
